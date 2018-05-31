package com.classparser.bytecode.impl.assembly.attach;

import com.classparser.bytecode.impl.utils.ClassFileUtils;
import com.classparser.bytecode.impl.utils.ClassNameUtils;
import com.classparser.exception.ByteCodeParserException;

import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

public class ClassDefiner {

    private final Method defineClassMethod;

    public ClassDefiner() {
        try {
            Class<?>[] parameterTypes = {String.class, byte[].class, int.class, int.class, ProtectionDomain.class};
            defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", parameterTypes);
        } catch (ReflectiveOperationException exception) {
            throw new ByteCodeParserException("Can't obtains define class method!", exception);
        }
    }

    public void defineClass(byte[] byteCode, URL location) {
        String className = ClassNameUtils.normalizeFullName(ClassNameUtils.getClassName(byteCode));
        try {
            ProtectionDomain protectionDomain = createProtectionDomainForToolClasses(location, getCurrentClassLoader());
            defineClassMethod.setAccessible(true);
            defineClassMethod.invoke(getCurrentClassLoader(), className,
                    byteCode, 0, byteCode.length, protectionDomain);
            defineClassMethod.setAccessible(false);
        } catch (ReflectiveOperationException exception) {
            throw new ByteCodeParserException("Can't load class with name: " + className, exception);
        }
    }

    protected ProtectionDomain createProtectionDomainForToolClasses(URL codeLocation, ClassLoader classLoader) {
        ProtectionDomain protectionDomain = getClass().getProtectionDomain();
        Principal[] principals = protectionDomain.getPrincipals();
        PermissionCollection permissions = protectionDomain.getPermissions();

        Certificate[] certificates = protectionDomain.getCodeSource().getCertificates();
        CodeSource codeSource = new CodeSource(codeLocation, certificates);

        return new ProtectionDomain(codeSource, permissions, classLoader, principals);
    }

    private ClassLoader getCurrentClassLoader() {
        return ClassFileUtils.getClassLoader(getClass());
    }
}