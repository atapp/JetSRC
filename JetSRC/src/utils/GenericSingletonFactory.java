package utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GenericSingletonFactory {
	public static Map<String,Object> objectFactory = new HashMap<String, Object>();

    public static <T> T getInstance(Class<T> c){
        String key = c.toString();
        Object instance= objectFactory.get(key);
        if(instance == null){
            try{
                instance = c.getDeclaredConstructor().newInstance();
                objectFactory.put(key, instance);
            }catch(IllegalAccessException | InstantiationException e){
                throw new RuntimeException("Exception while creating singleton instance for class : "+key+" - Exception Message : "+e);
            } catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
            
        }
        return c.cast(instance);
    }
}
