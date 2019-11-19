package com.haven.simplej.rpc.center.service.rpc;

import com.haven.simplej.rpc.center.model.SequenceInfoModel;
import com.haven.simplej.rpc.center.service.ISequenceService;
import com.haven.simplej.rpc.center.service.SequenceInfoService;
import com.haven.simplej.time.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 序列号生成器
 * @author: havenzhang
 * @date: 2018/6/10 23:41
 * @version 1.0
 */
@Service
@Slf4j
public class SequenceServiceImpl implements ISequenceService {

	@Autowired
	private SequenceInfoService sequenceInfoService;

	@Override
	public int register(String namespace, String seqKey, int step) {
		log.debug("register sequence ,namespace:{},seqKey:{}，step:{}", namespace, seqKey, step);

		SequenceInfoModel model = new SequenceInfoModel();
		model.setVersion(1);
		model.setSeqKey(seqKey);
		model.setNamespace(namespace);
		model.setStep(step);
		model.setSeqValue(1L);//默认起步值
		model.setCreateTime(DateUtils.getTimestamp(new Date()));
		model.setUpdateTime(DateUtils.getTimestamp(new Date()));
		model.setUpdatedBy("admin");
		model.setCreatedBy("admin");
		return sequenceInfoService.save(model);
	}

	@Override
	@Transactional
	public long getNextShortSeqNo(String namespace, String seqKey, int length) {
		SequenceInfoModel model = new SequenceInfoModel();
		model.setSeqKey(seqKey);
		model.setNamespace(namespace);
		model = sequenceInfoService.get(model);

		//下一个取值
		long nextValue = model.getSeqValue() + model.getStep() + length + 1;

		SequenceInfoModel model2 = new SequenceInfoModel();
		model2.setSeqValue(nextValue);
		model2.setStep(length);
		sequenceInfoService.update(model2);
		return nextValue;
	}

	@Override
	public String getNextLongSeqNo(String namespace, String seqKey) {

		return null;
	}
}
