package bean.pwr.imskamieskiego.GUI;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * Wrapper for animations
 */
public class AnimationAdapter {
    private Context context;
    private int animationID;

    /**
     * Create animation adapter object
     * @param context context of activity
     * @param animationID ID of animation from resources
     */
    public AnimationAdapter(Context context, int animationID) {
        this.context = context;
        this.animationID = animationID;
    }

    /**
     * Start animation for given view. When animation end, methods from listener will be call.
     * @param view view for which animation will be started
     * @param listener is called after animation end
     */
    public void startAnimation(final View view, @Nullable final AnimationEndListener listener){
        Animation animation = AnimationUtils.loadAnimation(context, animationID);
        view.setAnimation(animation);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener!=null) {
                    listener.onAnimationEnd(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public interface AnimationEndListener{
        void onAnimationEnd(View view);
    }
}
