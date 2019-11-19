package com.haven.simplej.db.datasource.balance.strategy;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.haven.simplej.db.model.DataSourceLookupKey;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 轮询策略
 * @author haven.zhang
 * @date 2019/1/22.
 */
public class RoundRobinLoadBalanceStrategy implements LoadBalanceStrategy<DataSourceLookupKey> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoundRobinLoadBalanceStrategy.class);

	private static final int MIN_LB_FACTOR = 1;

	private List<DataSourceLookupKey> targets;
	private int currentPos;

	private Map<DataSourceLookupKey, Integer> currentTargets;
	private Map<DataSourceLookupKey, Integer> failedTargets;

	public RoundRobinLoadBalanceStrategy(Map<DataSourceLookupKey, Integer> lbFactors) {
		Assert.notNull(lbFactors);
		currentTargets = Collections.synchronizedMap(lbFactors);
		Assert.notEmpty(lbFactors);
		failedTargets = Collections.synchronizedMap(new HashMap<DataSourceLookupKey, Integer>(currentTargets.size()));
		reInitTargets(currentTargets);
	}

	private void reInitTargets(Map<DataSourceLookupKey, Integer> lbFactors) {
		targets = initTargets(lbFactors);
		// Assert.notEmpty(targets);
		if (CollectionUtils.isEmpty(targets)) {
			LOGGER.error("targets is empty");
		}
		currentPos = 0;
	}

	public List<DataSourceLookupKey> initTargets(Map<DataSourceLookupKey, Integer> lbFactors) {
		if (MapUtils.isEmpty(lbFactors)) {
			return null;
		}

		fixFactor(lbFactors);

		Collection<Integer> factors = lbFactors.values();

		int min = Collections.min(factors);
		if (min > MIN_LB_FACTOR && canModAll(min, factors)) {
			return buildBalanceTargets(lbFactors, min);
		}

		return buildBalanceTargets(lbFactors, MIN_LB_FACTOR);
	}

	protected synchronized List<DataSourceLookupKey> getTargets() {
		if (targets == null) {
			targets = new ArrayList<DataSourceLookupKey>();
		}
		return targets;
	}

	private void fixFactor(Map<DataSourceLookupKey, Integer> lbFactors) {
		Set<Map.Entry<DataSourceLookupKey, Integer>> setEntries = lbFactors.entrySet();
		for (Map.Entry<DataSourceLookupKey, Integer> entry : setEntries) {
			if (entry.getValue() < MIN_LB_FACTOR) {
				entry.setValue(MIN_LB_FACTOR);
			}
		}
	}

	private boolean canModAll(int base, Collection<Integer> factors) {
		for (Integer integer : factors) {
			if (integer % base != 0) {
				return false;
			}
		}
		return true;
	}

	private List<DataSourceLookupKey> buildBalanceTargets(Map<DataSourceLookupKey, Integer> lbFactors, int baseFactor) {
		Set<Map.Entry<DataSourceLookupKey, Integer>> setEntries = lbFactors.entrySet();
		List<DataSourceLookupKey> targets = new ArrayList<DataSourceLookupKey>();
		for (Map.Entry<DataSourceLookupKey, Integer> entry : setEntries) {
			int factor = entry.getValue() / baseFactor;

			for (int i = 0; i < factor; i++) {
				targets.add(entry.getKey());
			}
		}
		return targets;
	}

	@Override
	public synchronized DataSourceLookupKey elect() {
		if (CollectionUtils.isEmpty(this.targets)) {
			return null;
		}
		if (currentPos >= targets.size()) {
			currentPos = 0;
		}
		return targets.get(currentPos++);
	}

	@Override
	public synchronized void removeTarget(DataSourceLookupKey key) {
		if (currentTargets.containsKey(key)) {
			failedTargets.put(key, currentTargets.get(key));
			currentTargets.remove(key);
			reInitTargets(currentTargets);
		}
	}

	@Override
	public synchronized void recoverTarget(DataSourceLookupKey key) {
		if (failedTargets.containsKey(key)) {
			currentTargets.put(key, failedTargets.get(key));
			failedTargets.remove(key);
			reInitTargets(currentTargets);
		}
	}

}