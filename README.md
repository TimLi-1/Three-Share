# SDD-Project

**Group Name:** Recycle Bin\
**Motto:** Where we belong!\
**Communication Platform:** Slack

[Class Website](https://sites.google.com/site/rpisdd/home)\
[Peer Evaluation Document](https://docs.google.com/document/d/1D_z4sYiMz3xodhVX8bbK07O_-YSRFxAe030zjJ8njQo/edit?usp=sharing)\
[Status Report](https://docs.google.com/document/d/1Gq-S-pvJvnfisQ2nGSs6HPflyAuvZhsY8yloKFDakWo/edit?usp=sharing)

**GuideLine:**
Environment: Java8\
\
Description:\
This simple chat room support both group message and private massage. All the messages first go through the sever and sever will dispatch them. 

How to Run (in the case of tcp):
1. Build 
2. Run Server.java, which is a registration and dispatch center: 
3. Run ClientThread.java as clientA
4. Run ClientB.java as clientB

**Tech stack:**\
The communication between server and client used __web socket__;\
**Multithread** is used: When the server monitor a request, server will assign this task to a new subthread while rhe main thread remain listening. 
 
