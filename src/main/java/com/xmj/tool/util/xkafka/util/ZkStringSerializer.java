package com.xmj.tool.util.xkafka.util;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class ZkStringSerializer implements ZkSerializer {
	public byte[] serialize(Object data) throws ZkMarshallingError {
		String value = data + "";
		try {
			return value.getBytes("UTF-8");
		} catch (Exception e) {
			throw new ZkMarshallingError("zk serialize error:", e);
		}

	}

	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		String value = null;
		try {
			value = new String(bytes, "UTF-8");
		} catch (Exception e) {
			throw new ZkMarshallingError("zk deserialize error:", e);
		}
		return value;
	}
}
