package it.smartcommunitylab.scoengine.exception;

public class SCOCustomException extends Exception {

	public String erroMsg;
	public int errorCode;
	public Response body;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SCOCustomException(String msg) {
		super(msg);
	}

	public SCOCustomException(int errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
		this.erroMsg = msg;
		this.body = new Response<Void>(errorCode, msg);
	}

	public Response getBody() {
		return body;
	}

}