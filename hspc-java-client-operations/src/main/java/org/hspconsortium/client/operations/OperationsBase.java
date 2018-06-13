package org.hspconsortium.client.operations;

import org.hspconsortium.client.operations.model.HSPCClientOperationsSession;

public class OperationsBase {

	private HSPCClientOperationsSession session;
	
	public OperationsBase(HSPCClientOperationsSession session){
		this.session = session;
	}

	public HSPCClientOperationsSession getSession() {
		return session;
	}
}
