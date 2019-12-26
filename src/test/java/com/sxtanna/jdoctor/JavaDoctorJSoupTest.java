package com.sxtanna.jdoctor;

import com.sxtanna.jdoctor.base.JavaDoctorTest;

public final class JavaDoctorJSoupTest extends JavaDoctorTest
{

	private static final String JSOUP_JAVADOCS_URL = "https://jsoup.org/apidocs/index-all.html";


	@Override
	protected String url()
	{
		return JSOUP_JAVADOCS_URL;
	}


	@Override
	protected String case0Type()
	{
		return "Document";
	}

	@Override
	protected String case0Desc()
	{
		return "A HTML Document.";
	}

	@Override
	protected String case1Type()
	{
		return "Node";
	}

	@Override
	protected String case1Desc()
	{
		return "The base, abstract Node model.";
	}

}