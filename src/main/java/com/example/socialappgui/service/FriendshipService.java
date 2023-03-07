package com.example.socialappgui.service;

import com.example.socialappgui.domain.Friendship;
import com.example.socialappgui.domain.Request;
import com.example.socialappgui.domain.RequestType;
import com.example.socialappgui.domain.User;
import com.example.socialappgui.events.ChangeEventType;
import com.example.socialappgui.events.EntityChangeEvent;
import com.example.socialappgui.repository.FriendshipDBRepo;
import com.example.socialappgui.repository.GenericRepo;
import com.example.socialappgui.observer.Observable;
import com.example.socialappgui.observer.Observer;
import com.example.socialappgui.repository.UserDBRepo;

import java.time.LocalDateTime;
import java.util.*;

/**
 * service used to give instructions to the friendship...and user repositories
 */
public class FriendshipService{
    private FriendshipDBRepo friendshipRepo;
    private UserDBRepo userRepo;

    /**
     * the class constructor
     * @param friendshipRepo - the friendship repository that will be called in methods
     * @param userRepo - the user repository that will be called in methods
     */
    public FriendshipService(FriendshipDBRepo friendshipRepo, UserDBRepo userRepo)
    {
        this.friendshipRepo = friendshipRepo;
        this.userRepo = userRepo;
    }

    /**
     * method that will create a new 'Friendship' object and attempt to save it in the repository
     * @param idU1 - id of the first user
     * @param idU2 - id of the second user
     * @return - 1, if the save failed
     *         - 0, if the save was successful
     */
    public int save(Long idU1, Long idU2, LocalDateTime friendsSince, String status, RequestType requestType)
    {
        Friendship friendship = new Friendship(idU1,idU2,friendsSince,status, requestType);
        if(friendshipRepo.save(friendship)!=null)
            return 1;
        return 0;
    }

    /**
     * method that will attempt to delete the friendship with the given id from the repository
     * as well as the former friend from each other's lists
     * @param id - id of the desired friendship
     * @return - 1, if the deletion failed
     *         - 0, if it was successful
     */
    public int delete(Long id)
    {
        Friendship friendship = friendshipRepo.delete(id);
        if(friendship==null)
            return 1;
        return 0;
    }

    /**
     * method that will attempt to modify the status of the friendship with the given id
     * @param id - the id of the desired friendship
     * @param newStatus - the desired new status
     * @return - 1, if the operation failed
     *         - 0, if it was successful
     */
    public int updateStatus(Long id,String newStatus)
    {
        Friendship original = friendshipRepo.findOne(id);
        if(original==null)
            return 1;
        Friendship friendship = new Friendship(original.getIdUser1(),original.getIdUser2(),original.getFriendsSince(),newStatus,original.getRequest());
        friendship.setID(id);
        if(friendshipRepo.update(friendship)!=null)
            return 1;
        return 0;
    }

    public int updateRequest(Long id, RequestType newRequestType)
    {
        Friendship original = friendshipRepo.findOne(id);
        if(original==null)
            return 1;
        Friendship friendship = new Friendship(original.getIdUser1(),original.getIdUser2(),original.getFriendsSince(),original.getDescription(), newRequestType);
        friendship.setID(id);
        if(friendshipRepo.update(friendship)!=null)
            return 1;
        return 0;
    }


    /**
     *  method used to obtain the friendships in the memory
     * @return - all the friendships, stored into an 'Iterable' object
     */
    public Iterable<Friendship> getFriendshipsList()
    {
        return friendshipRepo.findAll();
    }

    public List<Request> getFriends(Long id)
    {
        Iterable<Friendship> friends = friendshipRepo.findFriends(id);

        List<Request> req= new ArrayList<>();
        for (Friendship f: friends)
        {
            Long iD;
            if(Objects.equals(f.getIdUser1(), id))
                iD=f.getIdUser2();
            else
                iD=f.getIdUser1();
            String name = userRepo.findOne(iD).getName();
            req.add(new Request(iD,f.getID(),name,f.getFriendsSince(),f.getDescription()));
        }

        return req;
    }


    public List<Request> getSentRequests(Long id)
    {
        Iterable<Friendship> friends = friendshipRepo.findSentRequests(id);

        List<Request> req= new ArrayList<>();
        for (Friendship f: friends)
        {
            Long iD=f.getIdUser2();
            String name = userRepo.findOne(iD).getName();
            req.add(new Request(iD,f.getID(),name,f.getFriendsSince(),f.getDescription()));
        }

        return req;
    }

    public List<Request> getReceivedRequests(Long id)
    {
        Iterable<Friendship> friends = friendshipRepo.findReceivedRequests(id);

        List<Request> req= new ArrayList<>();
        for (Friendship f: friends)
        {
            Long iD=f.getIdUser1();
            String name = userRepo.findOne(iD).getName();
            req.add(new Request(iD,f.getID(),name,f.getFriendsSince(),f.getDescription()));
        }

        return req;
    }

    public List<Request> getOthers(Long id)
    {
        List<Request> notRequests = new ArrayList<>();

        List<Request> friends = getFriends(id);
        List<Request> sent = getSentRequests(id);
        List<Request> received = getReceivedRequests(id);

        List<String> interacted = new ArrayList<>();
        for(Request r: friends)
        {
            interacted.add(r.getFriendName());
        }
        for(Request r: sent)
        {
            interacted.add(r.getFriendName());
        }
        for(Request r: received)
        {
            interacted.add(r.getFriendName());
        }

        Iterable<User> users = userRepo.findAll();
        for(User u: users)
        {
            if(!interacted.contains(u.getName()) && !Objects.equals(u.getID(), id))
                notRequests.add(new Request(u.getID(),null,u.getName(),null,null));
        }

        return notRequests;

    }

    /**
     * method used in constructor, will add the id's of each user's friends in their lists
     */
    public void loadFriends(User user)
    {
        for (Friendship friendship: friendshipRepo.findAll())
        {
            if(Objects.equals(user.getID(), friendship.getIdUser1()))
                user.addFriend(friendship.getIdUser2());
            if(Objects.equals(user.getID(), friendship.getIdUser2()))
                user.addFriend(friendship.getIdUser1());
        }
    }

    /**
     * method that deletes every friendship that included the user with the given id
     * @param id - id of the user
     */
    public void deleteFriendshipsWithDeletedUser(Long id)
    {
        Iterable<Friendship> friendships = friendshipRepo.findAll();
        List<Friendship> friendshipList = new ArrayList<>();
        friendships.forEach(friendshipList::add);
        for(Friendship friendship: friendshipList)
        {
            if(Objects.equals(friendship.getIdUser1(), id)
                    || Objects.equals(friendship.getIdUser2(), id))
                friendshipRepo.delete(friendship.getID());
        }
    }

    /**
     * method used to check a user as visited and to go through every accessible user
     * @param entry - the user that is visited
     * @param visited - the Map that keeps the visited status of each user
     */
    void visiting(Map.Entry<User,Boolean> entry, Map<User,Boolean> visited, ArrayList<User> components)
    {
        entry.setValue(true);
        User user = entry.getKey();
        components.add(user);
        List<Long> friendList = user.getFriends();
        //System.out.println(friendList);
        for(Long friendID: friendList)
        {
            User friend = userRepo.findOne(friendID);
            for(Map.Entry<User,Boolean> possibleFriend: visited.entrySet())
                if(Objects.equals(possibleFriend.getKey(),friend) && !possibleFriend.getValue())
                    visiting(possibleFriend,visited, components);

        }
    }

    /**
     * method used to obtain the  communities in the network
     * @return - the  communities
     */
    public ArrayList<ArrayList<User>> communities()
    {
        ArrayList<ArrayList<User>> communities = new ArrayList<>();

        int n=0;
        Map<User,Boolean> visited = new HashMap<User,Boolean>();
        Iterable<User> userIterable = userRepo.findAll();
        for (User user: userIterable) {
            loadFriends(user);
            // System.out.println(user.getFriends().size());
            visited.put(user,false);
        }
        for(Map.Entry<User,Boolean> entry: visited.entrySet())
        {
            ArrayList<User> components = new ArrayList<>();
            if(!entry.getValue())
            {
                visiting(entry, visited,components);
                communities.add(components);
            }

        }
        return communities;
    }

    /**
     * method used to determine the number of communities
     * @return - the number of communities
     */
    public int numberCommunities()
    {
        return communities().size();
    }

    /**
     * determines the most social community in the network
     * @return - an arraylist containing the users of the most social community
     */
    public ArrayList<User> mostSociableCommunity()
    {
        ArrayList<Integer> maxLength = new ArrayList<>();
        ArrayList<ArrayList<User>> communities = communities();
        ArrayList<User> possibleMSC = new ArrayList<>();
        for(ArrayList<User> community: communities)
        {
            ArrayList<User> copy = new ArrayList<User>(community);
            ArrayList<User> possibleLR = new ArrayList<>();
            longestPathBacktracking(community, possibleMSC, copy, possibleLR, maxLength);
        }
        return possibleMSC;
    }

    /**
     * backtracking for determining the longest path
     * @param community - the list of users we will be interested in
     * @param possibleMSC - retains what is at the moment the most social community
     * @param communityCopy - a copy of the original community, because the original will be modified
     * @param possibleLR - what is at that moment possibly the longest road/path
     * @param maxLength - an array retaining at the moment the length of the longest road
     */
    public void longestPathBacktracking(ArrayList<User> community,ArrayList<User> possibleMSC,ArrayList<User> communityCopy, ArrayList<User> possibleLR, ArrayList<Integer> maxLength)
    {
        for(User user: community)
        {
            if(!possibleLR.contains(user))
            {
                possibleLR.add(user);
                if(possibleLR.size()>maxLength.size())
                {
                    maxLength.clear();
                    possibleMSC.clear();
                    for(int i=0;i< possibleLR.size();i++)
                        maxLength.add(1);
                    possibleMSC.addAll(communityCopy);
                }
                ArrayList<Long> friendIDs = (ArrayList<Long>) user.getFriends();
                ArrayList<User> friends = new ArrayList<>();
                for(Long friendID: friendIDs)
                {
                    friends.add(userRepo.findOne(friendID));
                }
                longestPathBacktracking(friends, possibleMSC, communityCopy, possibleLR, maxLength);
                possibleLR.remove(user);
            }
        }
    }


}
