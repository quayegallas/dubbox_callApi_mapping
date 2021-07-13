package execute;

import execute.handler.Handler;

/**
 * @author Aaron
 * @since 2020/11/8 16:54
 * <p>描述：</p>
 */
public class GoToImplExecute implements Execute {

    private final Handler handler;

    public GoToImplExecute(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void doExecute() {
        handler.doHandle();
    }

}
