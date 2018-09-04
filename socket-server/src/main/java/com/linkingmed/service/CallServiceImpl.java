package com.linkingmed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkingmed.server.SocketServer;

@Service
public class CallServiceImpl implements CallService {
	
	@Autowired
	private SocketServer socket;
	
}
