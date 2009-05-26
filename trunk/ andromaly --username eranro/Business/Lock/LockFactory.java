package andromaly.main.Business.Lock;

public class LockFactory {
	private static LockInterface _instance = null;
	
	private LockFactory(){}
	
	public static LockInterface getInstance(){
		if (_instance == null){
			_instance = new MenuLock();
		}
		return _instance;
	}
}
