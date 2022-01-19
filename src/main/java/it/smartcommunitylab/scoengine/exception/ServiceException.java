package it.smartcommunitylab.scoengine.exception;

public class ServiceException extends Exception {
	private static final long serialVersionUID = -6197169937762469426L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}
}
