package net.bridgesapi.core.api.friends;

import net.bridgesapi.api.BukkitBridge;
import net.bridgesapi.api.friends.FriendsManager;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * This file is a part of the SamaGames project
 * This code is absolutely confidential.
 * Created by zyuiop
 * (C) Copyright Elydra Network 2015
 * All rights reserved.
 */
public class FriendsManagementDB implements FriendsManager {

	private final BukkitBridge api;

	public FriendsManagementDB(BukkitBridge api) {
		this.api = api;
	}

	@Override
	public boolean areFriends(UUID p1, UUID p2) {
		return uuidFriendsList(p1).contains(p2);
	}

	@Override
	public List<String> namesFriendsList(UUID asking) {
		ArrayList<String> playerNames = new ArrayList<>();

		for (UUID id : uuidFriendsList(asking)) {
			String name = api.getUUIDTranslator().getName(id, true);
			if (name == null) {
				continue;
			}
			playerNames.add(name);
		}
		return playerNames;
	}

	@Override
	public List<UUID> uuidFriendsList(UUID asking) {
		ArrayList<UUID> playerIDs = new ArrayList<>();

		Jedis jedis = api.getResource();
		for (String data : jedis.lrange("friends:"+asking, 0, -1)) {
			if (data == null || data.equals("")) {
				jedis.lrem("friends:"+asking, 0, data);
				continue;
			}

			try  {
				UUID id = UUID.fromString(data);
				playerIDs.add(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jedis.close();
		return playerIDs;
	}

	public Map<UUID, String> associatedFriendsList(UUID asking) {
        HashMap<UUID, String> ret = new HashMap<>();

        for (UUID id : uuidFriendsList(asking)) {
            String name = api.getUUIDTranslator().getName(id, true);
            if (name == null) {
                continue;
            }
            ret.put(id, name);
        }
        return ret;
    }

	@Override
	public List<String> requests(UUID asking) {
		String dbKey = "friendrequest:*:"+asking;
		ArrayList<String> playerNames = new ArrayList<>();

		Jedis jedis = api.getResource();
		for (String data : jedis.keys(dbKey)) {
			String[] parts = data.split(":");
			try  {
				UUID id = UUID.fromString(parts[1]);
				playerNames.add(api.getUUIDTranslator().getName(id, true));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jedis.close();
		return playerNames;
	}

	@Override
	public List<String> sentRequests(UUID asking) {
		String dbKey = "friendrequest:"+asking+":";
		ArrayList<String> playerNames = new ArrayList<>();

		Jedis jedis = api.getResource();
		for (String data : jedis.keys(dbKey)) {
			String[] parts = data.split(":");
			try  {
				UUID id = UUID.fromString(parts[1]);
				playerNames.add(api.getUUIDTranslator().getName(id, true));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		jedis.close();
		return playerNames;
	}

}
