package org.jboss.errai.ioc.tests.rebind;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.enterprise.util.TypeLiteral;

import org.jboss.errai.ioc.rebind.ioc.codegen.Context;
import org.jboss.errai.ioc.rebind.ioc.codegen.Statement;
import org.jboss.errai.ioc.rebind.ioc.codegen.Variable;
import org.jboss.errai.ioc.rebind.ioc.codegen.builder.LoopBuilder.LoopBodyBuilder;
import org.jboss.errai.ioc.rebind.ioc.codegen.builder.StatementBuilder;
import org.jboss.errai.ioc.rebind.ioc.codegen.exception.InvalidTypeException;
import org.jboss.errai.ioc.rebind.ioc.codegen.exception.OutOfScopeException;
import org.jboss.errai.ioc.rebind.ioc.codegen.exception.TypeNotIterableException;
import org.junit.Test;

/**
 * Tests the generation of loops using the {@link StatementBuilder} API.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class LoopBuilderTest extends AbstractStatementBuilderTest implements LoopBuilderTestResult {
    
    @Test
    public void testForeachLoop() throws Exception {
        Statement createObject = StatementBuilder.create()
            .newObject(String.class);

        Statement createAnotherObject = StatementBuilder.create()
            .newObject(Object.class);

        String foreachWithListOfStrings = StatementBuilder.create(
                Context.create().add(Variable.get("list", new TypeLiteral<List<String>>(){})))
            .loadVariable("list")
            .foreach("element")
            .generate();
         
        String foreachWithStringArray = StatementBuilder.create(
                Context.create().add(Variable.get("list", String[].class)))
            .loadVariable("list")
            .foreach("element")
            .execute(createObject)
            .generate();
            
        String foreachWithList = StatementBuilder.create(
                Context.create().add(Variable.get("list", List.class)))
            .loadVariable("list")
            .foreach("element")
            .execute(createObject)
            .execute(createAnotherObject)
            .generate();
        
        assertEquals(FOREACH_RESULT_STRING_IN_LIST, foreachWithListOfStrings);
        assertEquals(FOREACH_RESULT_STRING_IN_LIST_ONE_STATEMENT, foreachWithStringArray);
        assertEquals(FOREACH_RESULT_OBJECT_IN_LIST_TWO_STATEMENTS, foreachWithList);
    }
    
    @Test
    public void testForeachLoopWithUndefinedCollection() throws Exception {
      
        try {
            StatementBuilder.create()
                .loadVariable("list")
                .foreach("element", Integer.class)
                .generate();
  
            fail("Expected OutOfScopeException");
        } catch(OutOfScopeException oose) {
            // expected
        }
    }
    
    @Test
    public void testForeachLoopWithProvidedLoopVarType() throws Exception {
        Statement loop = StatementBuilder.create(
                Context.create().add(Variable.get("list", new TypeLiteral<List<String>>(){})))
            .loadVariable("list")
            .foreach("element", Object.class);
        
        assertEquals(FOREACH_RESULT_OBJECT_IN_LIST, loop.generate());
        
        try {
            StatementBuilder.create(
                    Context.create().add(Variable.get("list", new TypeLiteral<List<String>>(){})))
                .loadVariable("list")
                .foreach("element", Integer.class)
                .generate();
  
            fail("Expected InvalidTypeException");
        } catch(InvalidTypeException ite) {
            // expected
        }
    }
    
    @Test
    public void testNestedForeachLoops() throws Exception {
        Statement createObject = StatementBuilder.create().newObject(Integer.class);

        Statement outerLoop = StatementBuilder.create(
                Context.create().add(Variable.get("list", new TypeLiteral<List<String>>(){})))
            .loadVariable("list")
            .foreach("element")
            .execute(StatementBuilder.create(
                        Context.create().add(Variable.get("anotherList", new TypeLiteral<List<String>>(){})))
                    .loadVariable("anotherList")
                    .foreach("anotherElement")
                    .execute(createObject)
             );
                
        assertEquals(FOREACH_RESULT_NESTED_STRING_IN_LIST, outerLoop.generate());
    }
    
    @Test
    public void testForeachLoopWithInvalidCollectionType() throws Exception {
        
        try {
            StatementBuilder.create(
                    Context.create().add(Variable.get("list", String.class)))
                .loadVariable("list")
                .foreach("element")
                .generate();

            fail("Expected TypeNotIterableException");
        } catch(TypeNotIterableException tnie) {
            // expected
        }
    }
    
    @Test
    public void testForeachLoopWithInvoke() throws Exception {
        Statement loop = StatementBuilder.create(
                Context.create().add(Variable.get("map", Map.class)))
            .loadVariable("map")
            .invoke("keySet")
            .foreach("key");
    
        assertEquals(FOREACH_RESULT_KEYSET_LOOP, loop.generate());
    }
    
    @Test
    public void testForeachLoopWithLiterals() throws Exception {
        LoopBodyBuilder loopBody = StatementBuilder.create()
            .loadLiteral(new String[]{"s1","s2"})
            .foreach("s");
        
        Statement body = StatementBuilder.create(loopBody.getContext()).loadVariable("s").invoke("getBytes");
        Statement loop = loopBody.execute(body);
        assertEquals(FOREACH_RESULT_LITERAL_STRING_ARRAY, loop.generate());
        
        Context c = Context.create().add(Variable.get("s", String.class));
        loop = StatementBuilder.create(c)
                .loadLiteral(new String[]{"s1","s2"})
                .foreach("s")
                .execute(StatementBuilder.create(c).loadVariable("s").invoke("getBytes"));
    
        assertEquals(FOREACH_RESULT_LITERAL_STRING_ARRAY, loop.generate());
    }
}
