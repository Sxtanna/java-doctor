package com.sxtanna.jdoctor;

import com.sxtanna.jdoctor.base.JavaDoctorTest;

public final class JavaDoctorPaperTest extends JavaDoctorTest
{

	private static final String PAPER_JAVADOCS_URL = "https://papermc.io/javadocs/paper/1.15/index-all.html";

	@Override
	protected String url()
	{
		return PAPER_JAVADOCS_URL;
	}


	@Override
	protected String case0Type()
	{
		return "Player";
	}

	@Override
	protected String case0Desc()
	{
		return "Represents a player, connected or not";
	}


	@Override
	protected String case1Type()
	{
		return "AsyncPlayerChatEvent";
	}

	@Override
	protected String case1Desc()
	{
		return "This event will sometimes fire synchronously, depending on how it was triggered.";
	}

}