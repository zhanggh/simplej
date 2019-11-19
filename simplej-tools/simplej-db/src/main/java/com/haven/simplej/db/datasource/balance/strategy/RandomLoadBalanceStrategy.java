package com.haven.simplej.db.datasource.balance.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.haven.simplej.db.model.DataSourceLookupKey;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

/**
 * 随机负载
 * @author haven.zhang
 */
public class RandomLoadBalanceStrategy implements LoadBalanceStrategy<DataSourceLookupKey> {

	private List<DataSourceLookupKey> targets;
	private List<DataSourceLookupKey> failedTargets;
	private final Random random = new Random();

	public RandomLoadBalanceStrategy(List<DataSourceLookupKey> targets) {
		Assert.notNull(targets);
		this.targets = Collections.synchronizedList(targets);
		Assert.notEmpty(targets);
		failedTargets = Collections.synchronizedList(new ArrayList<DataSourceLookupKey>(targets.size()));
	}

	@Override
	public synchronized DataSourceLookupKey elect() {
		if (CollectionUtils.isEmpty(this.targets)) {
			return null;
		}
		return targets.get(random.nextInt(targets.size()));
	}

	@Override
	public synchronized void removeTarget(DataSourceLookupKey target) {
		if (targets.contains(target)) {
			targets.remove(target);
			failedTargets.add(target);
		}
	}

	@Override
	public synchronized void recoverTarget(DataSourceLookupKey target) {
		if (failedTargets.contains(target)) {
			targets.add(target);
			failedTargets.remove(target);
		}
	}
}
