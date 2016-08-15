package exception;

public class MessageOverflowException extends Exception {

	private static final long serialVersionUID = 1L;

	public MessageOverflowException() {
		super("内容过长，无法发送数据");
	}
}
