package com.kiselev.reflection.ui.impl.reflection.constants;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Created by Aleksei Makarov on 08/01/2017.
 */
public interface CastConstants {

    Class<Class> CLASS = Class.class;

    Class<AnnotatedParameterizedType> ANNOTATED_PARAMETERIZED_TYPE = AnnotatedParameterizedType.class;

    Class<AnnotatedArrayType> ANNOTATED_ARRAY_TYPE = AnnotatedArrayType.class;

    Class<ParameterizedType> PARAMETERIZED_TYPE = ParameterizedType.class;

    Class<GenericArrayType> GENERIC_ARRAY_TYPE = GenericArrayType.class;

    Class<TypeVariable> TYPE_VARIABLE = TypeVariable.class;

    Class<WildcardType> WILDCARD_TYPE = WildcardType.class;

    Class<AnnotatedWildcardType> ANNOTATED_WILDCARD_TYPE = AnnotatedWildcardType.class;

    Class<Member> MEMBER = Member.class;

    Class<Annotation> ANNOTATION = Annotation.class;
}
