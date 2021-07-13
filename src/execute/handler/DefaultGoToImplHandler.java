package execute.handler;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiElement;
import util.CustomNotifier;

/**
 * @author Aaron
 * @since 2020/11/8 16:20
 * <p>描述：</p>
 */
public class  DefaultGoToImplHandler extends BaseGoToImplHandler {
    private final AnActionEvent anActionEvent;
    private final PsiElement currentPsiElement;

    public DefaultGoToImplHandler(AnActionEvent anActionEvent, PsiElement currentPsiElement) {
        this.anActionEvent = anActionEvent;
        this.currentPsiElement = currentPsiElement;
    }

    @Override
    public void doHandle() {
        CustomNotifier.warn(anActionEvent.getProject(),"Not Support's Go To Implementation.");
    }

}
