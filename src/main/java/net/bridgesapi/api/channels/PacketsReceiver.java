package net.bridgesapi.api.channels;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public interface PacketsReceiver {

	public void receive(String channel, String packet);

}
