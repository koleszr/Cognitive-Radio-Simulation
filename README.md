# Cognitive radio simulation

This repository has been created to store the work I have been doing for my master thesis.
The overall goal is to simulate a dynamic spectrum access capable cognitive radio network that
uses game theoretical approaches. 

Essentially, a basic game consists of 3 main elements:
* players,
* strategy,
* utility function.

## Players
 
In this simulation players are secondary users (cognitive radio devices) who attempt to capture channels not being accessed by primary users, e.g. by those users who license the given channel.

In order to permit the secondary users to access channels, the primary users have to share them. This spectrum sharing supports two methods:
* underlay spectrum sharing: the secondary users can transmit on any channels but their transmit power is limited to keep the interference inside a given limit,
* overlay spectrum sharing: the secondary users can only transmit on empty channels.

In my thesis I will use the latter spectrum sharing approach.

## Strategy

In game theory, strategy is a set of actions that a player can use in the game. 
In this work, strategy represents whether a player wants to access a channel or not.
Before I describe the strategies that will be used, the timing mechanism of the system must be presented.

### Timing

The timing of the system can be divided in 3 parts.

The 1st one indicates the varying behavior of the primary users. This behavior varies slowly in time or fast but its average
slowly.

The 2nd, faster time scale represents the decision period in which the secondary users decide about channel access.

The last one is the fastest and it simulates a CSMA medium access, where secondary users who want to access the given channel generate a random back off time and wait while this time has not expired. After the expiration, the players attempt to access the channel if it is empty.


### Bibliography

[Decentralized dynamic spectrum access for cognitive radios: cooperative design of a non-cooperative game](http://ieeexplore.ieee.org/xpl/login.jsp?tp=&arnumber=4784355&url=http%3A%2F%2Fieeexplore.ieee.org%2Fxpls%2Fabs_all.jsp%3Farnumber%3D4784355)

