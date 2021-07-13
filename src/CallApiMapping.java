import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import execute.Execute;
import execute.GoToImplExecute;
import execute.handler.DefaultGoToImplHandler;
import execute.handler.MapperGoToImplHandler;
import util.CustomNotifier;
import util.PsiElementUtil;

public class CallApiMapping extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        PsiElement currentPsiElement = anActionEvent.getData(LangDataKeys.PSI_ELEMENT);
        if (currentPsiElement == null) {
            CustomNotifier.error(anActionEvent.getProject(), "No elements are selected!");
            return;
        }

        // 根据本插件的目标，这里被选择的元素可以是以下几种情况：
        // 1 Feign 接口上的方法，跳转到本模块或其它模块的接口方法实现上
        // 2 Mapper 接口，跳转到对应的 xml 文件
        // 3 Mapper 接口上的方法，跳转到对应的 xml 文件的 sql 上
        Execute execute;
       if (PsiElementUtil.isMapperMethod(currentPsiElement)) {
            // Mapper 接口方法跳转
            execute = new GoToImplExecute(new MapperGoToImplHandler(anActionEvent,
                    (PsiClass) currentPsiElement.getParent(), (PsiMethod) currentPsiElement));
        } else {
            // 其它情况处理
            execute = new GoToImplExecute(new DefaultGoToImplHandler(anActionEvent, currentPsiElement));
        }

        execute.doExecute();
    }
}
