package execute.handler;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.PsiShortNamesCache;
import org.apache.commons.lang3.StringUtils;
import util.CustomNotifier;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Aaron
 * @since 2020/11/8 16:21
 * <p>描述：</p>
 */
public class MapperGoToImplHandler extends BaseGoToImplHandler {
    private static final String MAPPER = "Mapper";

    private final AnActionEvent anActionEvent;
    private final PsiClass currentPsiClass;
    private final PsiMethod currentPsiMethod;

    public MapperGoToImplHandler(AnActionEvent anActionEvent, PsiClass currentPsiClass, PsiMethod currentPsiMethod) {
        this.anActionEvent = anActionEvent;
        this.currentPsiClass = currentPsiClass;
        this.currentPsiMethod = currentPsiMethod;
    }

    @Override
    public void doHandle() {
        super.doHandle();

        // 获取当前 Project
        Project project = anActionEvent.getProject();
        if (project == null) {
            System.out.println("Can't get the project by the action event.");
            return;
        }

        String mapperClassName = currentPsiClass.getName();
        if (StringUtils.isBlank(mapperClassName)) {
            CustomNotifier.error(anActionEvent.getProject(), "Can't get the name of the action's element.");
            return;
        }

        CustomNotifier.info(anActionEvent.getProject(), "It will go to the Mapper Xml of " + currentPsiClass.getName() + ".");


        String service_method = currentPsiMethod.getParameterList().getParameters()[0].getText();
        String service = service_method.split("_")[0];
        String method = service_method.split("_")[1];
        String serviceFile = upperCaseFirst(service)+"Service";
        // 根据名称查找映射文件
        PsiFile[] mapperXmlPsiFiles = PsiShortNamesCache.getInstance(project).getFilesByName(serviceFile + ".java");
        if (mapperXmlPsiFiles.length == 0) {
            return;
        }

        Module module = ModuleUtil.findModuleForFile(currentPsiClass.getContainingFile().getVirtualFile(), anActionEvent.getProject());
        for (PsiFile file : mapperXmlPsiFiles) {
            if (Objects.equals(module, ModuleUtil.findModuleForFile(file.getVirtualFile(), anActionEvent.getProject()))) {
                doLocate(project, method, file);
            }
        }
    }
    public static String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }
}
