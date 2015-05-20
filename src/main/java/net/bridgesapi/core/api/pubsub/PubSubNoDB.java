package net.bridgesapi.core.api.pubsub;

import net.bridgesapi.api.channels.*;
import net.bridgesapi.api.channels.Sender;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class PubSubNoDB implements PubSubAPI {
	@Override
	public void subscribe(String channel, PacketsReceiver receiver) {
		// RIEN.
	}

	@Override
	public void subscribe(String pattern, PatternReceiver receiver) {

	}

	@Override
	public void send(String channel, String message) {
		// RIEN.
	}

	@Override
	public void send(PendingMessage message) {

	}

	@Override
	public Sender getSender() {
		return PendingMessage::runAfter;
	}
}
