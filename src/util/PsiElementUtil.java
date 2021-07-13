package util;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import constant.QualifiedName;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Aaron
 * @since 2020/11/6 15:42
 * <p>描述：PsiElement 的工具类：
 * 1 获取属性、方法、类上面的注解
 * 2 获取属性、方法、类上面注解的属性值
 * 3 判断给定元素是否是接口、Mapper 接口或 Mapper 接口内容定义的方法</p>
 */
@SuppressWarnings("unused")
public class PsiElementUtil {

    /**
     * 获取类中属性上的注解
     *
     * @param psiElement              给定元素，该元素必须为属性
     * @param annotationQualifiedName 注解的限定名，即全类名
     * @return 注解
     */
    public static PsiAnnotation getAnnotationAtField(PsiElement psiElement, String annotationQualifiedName) {
        if (!(psiElement instanceof PsiField)) {
            return null;
        }

        PsiField psiField = (PsiField) psiElement;

        // 根据注解限定名获取指定注解
        return psiField.getAnnotation(annotationQualifiedName);
    }

    /**
     * 获取方法上的注解
     *
     * @param psiElement              给定元素，该元素必须为方法
     * @param annotationQualifiedName 注解的限定名，即全类名
     * @return 注解
     */
    public static PsiAnnotation getAnnotationAtMethod(PsiElement psiElement, String annotationQualifiedName) {
        if (!(psiElement instanceof PsiMethod)) {
            return null;
        }

        PsiMethod psiMethod = (PsiMethod) psiElement;

        // 根据注解限定名获取指定注解
        return psiMethod.getAnnotation(annotationQualifiedName);
    }

    /**
     * 获取类上的注解
     *
     * @param psiElement              给定元素，该元素可以为属性或方法
     * @param annotationQualifiedName 注解的限定名，即全类名
     * @return 注解
     */
    public static PsiAnnotation getAnnotationAtClass(PsiElement psiElement, String annotationQualifiedName) {
        // 如果是方法或变量，则获取父元素
        if (psiElement instanceof PsiField || psiElement instanceof PsiMethod) {
            psiElement = psiElement.getParent();
        }

        if (!(psiElement instanceof PsiClass)) {
            return null;
        }

        PsiClass psiClass = (PsiClass) psiElement;

        // 根据注解限定名获取指定注解
        return psiClass.getAnnotation(annotationQualifiedName);
    }

    /**
     * 获取类属性的注解中的属性的值
     *
     * @param psiElement              给定元素，该元素应该是一个类的属性
     * @param annotationQualifiedName 注解的限定名，即全类名
     * @param attributeName           注解属性的名称
     * @return 注解属性的值
     */
    public static String getAttributeValueOfAnnotationAtField(PsiElement psiElement,
                                                              String annotationQualifiedName,
                                                              String attributeName) {
        PsiAnnotation psiAnnotation = getAnnotationAtField(psiElement, annotationQualifiedName);
        return getAttributeName(attributeName, psiAnnotation);
    }

    /**
     * 获取方法上的注解中的属性的值
     *
     * @param psiElement              给定元素，该元素应该是一个类的方法
     * @param annotationQualifiedName 注解的限定名，即全类名
     * @param attributeName           注解属性的名称
     * @return 注解属性的值
     */
    public static String getAttributeValueOfAnnotationAtMethod(PsiElement psiElement,
                                                               String annotationQualifiedName,
                                                               String attributeName) {
        PsiAnnotation annotationAtMethod = getAnnotationAtMethod(psiElement, annotationQualifiedName);
        return getAttributeName(attributeName, annotationAtMethod);
    }

    /**
     * 获取类上的注解中的属性的值
     *
     * @param psiElement              给定元素，该元素应该是一个类
     * @param annotationQualifiedName 注解的限定名，即全类名
     * @param attributeName           注解属性的名称
     * @return 注解属性的值
     */
    public static String getAttributeValueOfAnnotationAtClass(PsiElement psiElement,
                                                              String annotationQualifiedName,
                                                              String attributeName) {
        PsiAnnotation annotationAtClass = getAnnotationAtClass(psiElement, annotationQualifiedName);
        return getAttributeName(attributeName, annotationAtClass);
    }

    /**
     * 获取指定元素 psiElement 的指定注解 annotationQualifiedName 的指定属性 attributeName 的值
     *
     * @param psiElement              可以是属性，方法，类
     * @param annotationQualifiedName 注解限定名
     * @param attributeName           注解中的属性名
     * @return 属性值
     */
    public static String getAttributeValue(PsiElement psiElement,
                                           String annotationQualifiedName,
                                           String attributeName) {
        return getAttributeValue(psiElement, annotationQualifiedName, attributeName, true);
    }

    /**
     * 获取指定元素 psiElement 的指定注解 annotationQualifiedName 的指定属性 attributeName 的值
     *
     * @param psiElement              可以是属性，方法，类
     * @param annotationQualifiedName 注解限定名
     * @param attributeName           注解中的属性名
     * @param isExtensible            是不允许扩展搜索注解
     * @return 属性值
     */
    public static String getAttributeValue(PsiElement psiElement,
                                           String annotationQualifiedName,
                                           String attributeName,
                                           boolean isExtensible) {
        if (psiElement instanceof PsiField) {
            String value = getAttributeValueOfAnnotationAtField(psiElement, annotationQualifiedName, attributeName);
            if (StringUtils.isBlank(value) && isExtensible) {
                value = getAttributeValueOfAnnotationAtMethod(psiElement, annotationQualifiedName, attributeName);
                if (StringUtils.isBlank(value)) {
                    value = getAttributeValueOfAnnotationAtClass(psiElement, annotationQualifiedName, attributeName);
                }
            }
            return value;
        }

        if (psiElement instanceof PsiMethod) {
            String value = getAttributeValueOfAnnotationAtMethod(psiElement, annotationQualifiedName, attributeName);
            if (StringUtils.isBlank(value) && isExtensible) {
                value = getAttributeValueOfAnnotationAtClass(psiElement, annotationQualifiedName, attributeName);
            }
            return value;
        }

        if (psiElement instanceof PsiClass) {
            return getAttributeValueOfAnnotationAtClass(psiElement, annotationQualifiedName, attributeName);
        }

        return null;
    }

    /**
     * 从指定注解中获取属性值
     *
     * @param attributeName 属性名称
     * @param psiAnnotation 指定注解
     * @return 属性值
     */
    private static String getAttributeName(String attributeName, PsiAnnotation psiAnnotation) {
        if (psiAnnotation == null) {
            return null;
        }

        // 获取 @RequestMapping 中的 url
        PsiAnnotationMemberValue value = psiAnnotation.findAttributeValue(attributeName);
        if (value == null) {
            return null;
        }

        // Text 里面多了引号
        return value.getText().replaceAll("\"", "");
    }

    /**
     * 判断 PsiElement 是否是接口：
     * 1 必须是 PsiClass
     * 2 必须是 Interface
     *
     * @param psiElement 给定元素
     * @return 是/否是接口
     */
    public static boolean isInterface(PsiElement psiElement) {
        return psiElement instanceof PsiClass && ((PsiClass) psiElement).isInterface();
    }

    /**
     * 是否是 Mapper 接口：
     * 1 必须是 PsiClass
     * 2 必须是 Interface
     * 3 必须使用 @Repository 注解，对于使用 @Component 或 @Service 标注的 Mapper 接口不予考虑
     *
     * @param psiElement 给定元素
     * @return 是/否是 Mapper 接口
     */
    public static boolean isMapperInterface(PsiElement psiElement) {
        return isInterface(psiElement) && getAnnotationAtClass(psiElement, QualifiedName.Annotation.CALLAPIMAPPING) != null;
    }

    /**
     * 是否是 Mapper 接口下定义的方法：
     * 1 必须是 PsiMethod
     * 2 其父元素必须是 Interface
     * 3 其父元素必须使用 @Repository 注解，对于使用 @Component 或 @Service 标注的 Mapper 接口不予考虑
     *
     * @param psiElement 给定元素
     * @return 是/否是 Mapper 接口方法
     */
    public static boolean isMapperMethod(PsiElement psiElement) {
        return psiElement instanceof PsiMethod && isMapperInterface(psiElement.getParent());
    }

}
