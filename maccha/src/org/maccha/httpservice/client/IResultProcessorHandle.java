package org.maccha.httpservice.client;

import org.maccha.httpservice.DataMessage;

public abstract class IResultProcessorHandle {
	public abstract void doHandle(DataMessage _requestDataMessage, DataMessage _responseDataMessage);
}
