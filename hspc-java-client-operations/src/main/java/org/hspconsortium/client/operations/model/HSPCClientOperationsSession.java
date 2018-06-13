package org.hspconsortium.client.operations.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hspconsortium.client.operations.util.NullChecker;
import org.hspconsortium.client.session.Session;

public class HSPCClientOperationsSession {

	private Map<String, Session> sessionsMap;
	
	public HSPCClientOperationsSession(Map<String, Session> sessionsMap){
		this.sessionsMap = sessionsMap;
	}
	
	public List<Session> getAllSessions(){
		return new ArrayList<>(sessionsMap.values());
	}
	
	public List<Session> getFilteredSessions(String... sessionKeys){
		
		if(NullChecker.isNullish(sessionKeys))
			return getAllSessions();
		
		List<String> keysAsList = Arrays.asList(sessionKeys);
		
		List<Session> filteredSessions = new ArrayList<>();
		
	    sessionsMap.entrySet().stream().filter(e -> keysAsList.contains(e.getKey())).forEach(e->filteredSessions.add(e.getValue()));
	    
	    if(NullChecker.isNullish(filteredSessions))
	    	throw new IllegalArgumentException("There was no session found with given key");
	    
	    return filteredSessions; 
	}
}
