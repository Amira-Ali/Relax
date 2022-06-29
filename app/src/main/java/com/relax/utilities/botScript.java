package com.relax.utilities;

import java.util.Random;

public class botScript {

    static String username = globalVariables.userName;

    public static String Introduce_Bot() {
        String[] RandomList = {"Hello " + username + ", my name is Lito and I'm here for your help.",
                "Hi " + username + ". First time can be intimidating but don't worry. I'm here for you.",
                "Welcome " + username + ", I'm Lito!",
                "Hey " + username + ", I thought I'll be alone forever!",
                "Hello sweetheart. thanks for coming.",
                "Hi dear, I thought I'll never see you!",
                "Finally, someone I can talk to!"};
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }

    public static String Greeting_FirstTime() {
        String[] RandomList = {"So, How are you today?",
                "So, What's up?",
                "So, How is everything going?",
                "So, How are you nowadays?",
                "So, How have you been lately?"};
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }


    public static String Greeting_SecondTime() {
        String[] RandomList = {"Hello Again, Happy to see you!",
                "Hi " + username + ", nice to see you again!",
                "Welcome back " + username + ", I've missed you!",
                "Hola " + username + ", I thought you forgot about me!"};
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }

    static String BotReplyOnPositive() {
        String[] RandomList = {"That's cool!",
                "Good to know!",
                "Great!",
                "That sounds nice " + username,
                "I hope you always be in a good mood!"};
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }

    static String BotReplyOnNegative() {
        String[] RandomList = {"I am sorry you feel that way.",
                "Oh " + username + "! I'm really sorry for you.",
                "It's okay " + username + ", I'm here for you..",
                "Please don't be sad, I'll help you",
                "Don't worry my dear, I'm here for you",
                "It's okay, I'm here for your help",
                "Oh " + username + "! believe me, you are not alone.",
                "You know " + username + ", sometimes just spitting out your feelings makes you better."};
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }

    static String Bot_SuggestList() {//user is in positive mood
        String[] RandomList = {"Do you prefer to talk about something specific?",
                "Are you here just for a chat?",
                "Would you like to talk about a specific subject?"
        };
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }

    public static String Bye_List() {
        String[] RandomList = {"Bye bye hun",
                "Goodbye dear",
                "Ciao",
                "See you soon",
                "See you","CU","I'll talk to you later","We'll talk later","okay then, bye bye",
                "I'll be waiting for you to comeback","Okay, I'll leave you alone", "Okay, I'll be going now","Okay, I'll be around",
                "Have a good day","Nice to talk to you","Please comeback soon"
        };
        int rnd = new Random().nextInt(RandomList.length);
        return RandomList[rnd];
    }


}