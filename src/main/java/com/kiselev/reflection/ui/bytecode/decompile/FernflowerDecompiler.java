package com.kiselev.reflection.ui.bytecode.decompile;

import com.kiselev.reflection.ui.bytecode.assembly.build.constant.Constants;
import com.kiselev.reflection.ui.bytecode.decompile.configuration.Configuration;
import com.kiselev.reflection.ui.bytecode.decompile.configuration.DecompilerConfiguration;
import org.jetbrains.java.decompiler.main.Fernflower;
import org.jetbrains.java.decompiler.main.decompiler.BaseDecompiler;
import org.jetbrains.java.decompiler.main.decompiler.PrintStreamLogger;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;
import org.jetbrains.java.decompiler.struct.ContextUnit;
import org.jetbrains.java.decompiler.struct.StructClass;
import org.jetbrains.java.decompiler.struct.StructContext;
import org.jetbrains.java.decompiler.struct.lazy.LazyLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

/**
 * Created by Алексей on 26.06.2017.
 */
public class FernflowerDecompiler implements IBytecodeProvider, IResultSaver, Decompiler {

    private byte[] byteCode;

    private Map<String, Object> configuration;

    private List<byte[]> nestedClasses = new ArrayList<>();

    private String source = "";

    public FernflowerDecompiler() {
    }

    @Override
    public String decompile(byte[] byteCode) {
        this.byteCode = byteCode;

        if (configuration == null) {
            configuration = getDefaultConfiguration();
        }
        IFernflowerLogger logger = new PrintStreamLogger(System.out);
        BaseDecompiler decompiler = new BaseDecompiler(this, this, configuration, logger);
        for (byte[] nestedClass : nestedClasses) {
            dirtyHack(decompiler, nestedClass);
        }
        dirtyHack(decompiler, byteCode);
        decompiler.decompileContext();

        return source;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration.getConfiguration();
    }

    @Override
    public void appendNestedClasses(Collection<byte[]> classes) {
        this.nestedClasses.addAll(classes);
    }

    @Override
    public void appendNestedClass(byte[] clazz) {
        this.nestedClasses.add(clazz);
    }

    @Override
    public byte[] getBytecode(String s, String s1) throws IOException {
        return byteCode;
    }

    private Map<String, Object> getDefaultConfiguration() {
        return DecompilerConfiguration.getBuilderConfiguration().getConfiguration();
    }

    public void dirtyHack(BaseDecompiler decompiler, byte[] byteCode) {
        try {
            Fernflower fernflower = getFernflower(decompiler);
            StructClass structClass = createClassStruct(byteCode);

            StructContext structContext = fernflower.getStructContext();
            Map<String, StructClass> classes = structContext.getClasses();
            classes.put(structClass.qualifiedName, structClass);

            Map<String, ContextUnit> units = getContextUnit(structContext);

            ContextUnit unit = getFalseContextUnit(fernflower);
            unit.addClass(structClass, structClass.qualifiedName + Constants.Suffix.CLASS_FILE_SUFFIX);
            units.put(structClass.qualifiedName, unit);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Fernflower getFernflower(BaseDecompiler decompiler) throws NoSuchFieldException, IllegalAccessException {
        Field fieldFernflower = decompiler.getClass().getDeclaredField("fernflower");
        fieldFernflower.setAccessible(true);
        return Fernflower.class.cast(fieldFernflower.get(decompiler));
    }

    public StructClass createClassStruct(byte[] byteCode) throws IOException {
        LazyLoader lazyLoader = new LazyLoader((s, s1) -> byteCode);
        StructClass structClass = new StructClass(byteCode, true, lazyLoader);
        LazyLoader.Link link = new LazyLoader.Link(1, structClass.qualifiedName, null);
        lazyLoader.addClassLink(structClass.qualifiedName, link);

        return structClass;
    }

    @SuppressWarnings("unchecked")
    public Map<String, ContextUnit> getContextUnit(StructContext context) throws NoSuchFieldException, IllegalAccessException {
        Field fieldUnits = StructContext.class.getDeclaredField("units");
        fieldUnits.setAccessible(true);
        return (Map<String, ContextUnit>) fieldUnits.get(context);
    }

    public ContextUnit getFalseContextUnit(Fernflower fernflower) {
        return new ContextUnit(0, null, "", true, this, fernflower);
    }

    @Override
    public void saveClassFile(String s, String s1, String s2, String source, int[] ints) {
        this.source = source;
    }

    @Override
    public void createArchive(String s, String s1, Manifest manifest) {
        //empty
    }

    @Override
    public void saveDirEntry(String s, String s1, String s2) {
        //empty
    }

    @Override
    public void copyEntry(String s, String s1, String s2, String s3) {
        //empty
    }

    @Override
    public void saveClassEntry(String s, String s1, String s2, String s3, String s4) {
        //empty
    }

    @Override
    public void closeArchive(String s, String s1) {
        //empty
    }

    @Override
    public void saveFolder(String s) {
        //empty
    }

    @Override
    public void copyFile(String s, String s1, String s2) {
        //empty
    }
}
