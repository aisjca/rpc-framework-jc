package rpc.framework.test;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.util.Recycler;

/**
 * @program rpc-framework-jc
 * @description:
 * @author: JC
 * @create: 2020/08/16 09:03
 */
public class RecyclerDemo {
    static class User{
        private final Recycler.Handle<User> handle;

        public User(Recycler.Handle<User> handle) {
            this.handle = handle;
        }

        public void recycle() {
            handle.recycle(this);
        }
    }

    private static final Recycler<User> RECYCLER = new Recycler<User>() {
        @Override
        protected User newObject(Handle<User> handle) {
            return new User(handle);
        }
    };

    public static void main(String[] args) {
        User user1 = RECYCLER.get();
        user1.recycle();
        User user2 = RECYCLER.get();
        user2.recycle();
        System.out.println(user1 == user2);
    }
}
