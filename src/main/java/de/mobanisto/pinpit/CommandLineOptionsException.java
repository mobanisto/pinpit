package de.mobanisto.pinpit;

public class CommandLineOptionsException extends Exception
{

	private static final long serialVersionUID = 7046715773461737791L;

	public CommandLineOptionsException(String message)
	{
		super(message);
	}

	public CommandLineOptionsException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
