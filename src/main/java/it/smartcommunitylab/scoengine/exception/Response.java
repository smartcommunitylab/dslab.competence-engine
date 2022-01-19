package it.smartcommunitylab.scoengine.exception;

public class Response<T> {

	private T data;
	private String errorMessage;
	private int errorCode;

	public Response() {
		super();
	}

	public Response(T data) {
		super();
		this.data = data;
	}

	public Response(int errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}