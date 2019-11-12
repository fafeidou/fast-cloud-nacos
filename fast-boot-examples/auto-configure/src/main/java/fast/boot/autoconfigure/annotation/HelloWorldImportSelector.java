package fast.boot.autoconfigure.annotation;

import fast.boot.autoconfigure.configuration.HelloWorldConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 *  HelloWorld {@link ImportSelector}
 */
public class HelloWorldImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{HelloWorldConfiguration.class.getName()};
    }
}
