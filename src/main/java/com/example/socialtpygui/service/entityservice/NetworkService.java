package com.example.socialtpygui.service.entityservice;

import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.Tuple;
import com.example.socialtpygui.domain.User;
import com.example.socialtpygui.repository.db.FriendshipDb;
import com.example.socialtpygui.repository.db.UserDb;
import com.example.socialtpygui.utils.Graph;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class NetworkService {
    private final UserDb userRepository;
    private final FriendshipDb friendshipRepository;

    public NetworkService(UserDb userDb, FriendshipDb friendshipDb)
    {
        this.friendshipRepository = friendshipDb;
        this.userRepository = userDb;
    }

    /**
     * @return a tuple with the nr of connect component of the network graph and the biggest one
     */
    public Tuple<Integer,List<User>> runNetworkGraph(){
        //the friendship list
        Iterable<Friendship> it= friendshipRepository.findAll();
        // make the graph with te user lists
        //need a list of users
        List<User> userList= new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        Graph<User> g= new Graph(userList);
        //add the edges
        it.forEach(frn->g.addEdge(userRepository.findOne(frn.getId().getLeft()),userRepository.findOne(frn.getId().getRight())));
        return g.nrConnectedComponentsNBiggest();
    }

    /**
     * @return the nr of communities
     */
    public int nrCommunities() {
        return runNetworkGraph().getLeft();
    }

    /**
     * @return the most social community
     */
    public Iterable<User> socialCommunity() {
        return runNetworkGraph().getRight()
                .stream().peek(user -> friendshipRepository.getFriends(user.getId()).forEach(s -> user.addFriend(userRepository.findOne(s.getLeft()))))
                .collect(toList());
    }
}
