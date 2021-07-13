package execute.handler;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;

/**
 * @author Aaron
 * @since 2020/11/8 17:02
 * <p>描述：</p>
 */
public abstract class BaseGoToImplHandler implements GoToImplHandler {

    @Override
    public void doGoToImpl() {

    }

    @Override
    public void doHandle() {

    }

    /**
     * 获取匹配内容在给定文件的行号
     *
     * @param file         给定文件
     * @param matchContent 给定的匹配内容
     * @return 行号
     */
    public final int getLineNumber(PsiFile file, String matchContent) {
        if (StringUtils.isBlank(matchContent)) {
            return 0;
        }

        String[] lineContents = file.getText().split("\n");
        for (int i = 0; i < lineContents.length; i++) {
            if (StringUtils.isNotBlank(lineContents[i])) {
                if (lineContents[i].contains("id=\"" + matchContent + "\"")
                        || lineContents[i].contains("\"" + matchContent + "\"")) {
                    // 对于 Mapper 映射文件来说，匹配的行的格式应该是 id="listBaseUser"
                    // 对于 Feign 方法的跳转来说，匹配的行的格式应该是 "listBaseUser"
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * 定位文件，且在内容匹配时精确到行
     *
     * @param project      当前工程
     * @param matchContent 匹配内容
     * @param file         给定的匹配文件
     * @return 是/否成功定位
     */
    protected boolean doLocate(Project project, String matchContent, PsiFile file) {
        OpenFileDescriptor descriptor = new OpenFileDescriptor(project, file.getVirtualFile());
        // 打开文件
        Editor editor = FileEditorManager.getInstance(project)
                .openTextEditor(descriptor, true);
        if (editor == null) {
            return false;
        }

        // 定位
        CaretModel caretModel = editor.getCaretModel();
        LogicalPosition logicalPosition = caretModel.getLogicalPosition();
        logicalPosition.leanForward(true);
        // 移动到目标行
        caretModel.moveToLogicalPosition(new LogicalPosition(getLineNumber(file, matchContent), logicalPosition.column));
        // 将目标行滚动到屏幕中央
        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER_DOWN);
        // 移除如果有选择的内容
        editor.getSelectionModel().removeSelection();

        return true;
    }

}
