/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.extension.siddhi.execution.unitconversion.mass;

import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.ReturnAttribute;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.exception.SiddhiAppValidationException;

import java.util.Map;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Mass;

import static tec.units.ri.unit.Units.KILOGRAM;

/**
 * Siddhi Function for UnitConversion Kilogram to Pound
 */
@Extension(
        name = "kgTolb",
        namespace = "unitconversion",
        description = "Converts the input kilograms into pounds",
        parameters = @Parameter(
                name = "p1",
                description = "The value needed to be converted from kilograms into pounds",
                type = {DataType.INT, DataType.LONG, DataType.FLOAT, DataType.DOUBLE}),
        returnAttributes = @ReturnAttribute(
                description = "The value converted from kilograms to pounds",
                type = {DataType.DOUBLE}),
        examples = @Example(
                description = "The kilogram values from UnitConversionForKilogramToPoundStream will be " +
                        "converted to pounds and inserted in to the OutMediationStream",
                syntax = "define stream UnitConversionForKilogramToPoundStream (inValue int); \n" +
                        "from UnitConversionForKilogramToPoundStream \n" +
                        "select unitconversion:kgTolb(inValue) as UnitConversionValue \n" +
                        "insert into OutMediationStream;"
        )
)
public class KilogramToPound extends FunctionExecutor {

    private UnitConverter converter;

    /**
     * The initialization method for KilogramToPound, this method will be called before the other methods
     *
     * @param attributeExpressionExecutors the executors of each function parameter
     * @param siddhiAppContext         the context of the execution plan
     */
    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ConfigReader reader, SiddhiAppContext
            siddhiAppContext) {
        Unit<Mass> pound = KILOGRAM.multiply(4.535924E-01);
        converter = KILOGRAM.getConverterTo(pound);
        if (attributeExpressionExecutors.length != 1) {
            throw new SiddhiAppValidationException(
                    "Invalid no of arguments passed to unitconversion:kgTolb() function, " +
                    "required 1, but found " + attributeExpressionExecutors.length);
        }
        Attribute.Type attributeType = attributeExpressionExecutors[0].getReturnType();
        if (!((attributeType == Attribute.Type.DOUBLE)
                || (attributeType == Attribute.Type.INT)
                || (attributeType == Attribute.Type.FLOAT)
                || (attributeType == Attribute.Type.LONG))) {
            throw new SiddhiAppValidationException("Invalid parameter type found " +
                    "for the argument of UnitConversion function, " +
                    "required " + Attribute.Type.INT + " or " + Attribute.Type.LONG +
                    " or " + Attribute.Type.FLOAT + " or " + Attribute.Type.DOUBLE +
                    ", but found " + attributeType.toString());
        }
    }

    /**
     * The main execution method which will be called upon event arrival
     * when there are more than one function parameter
     *
     * @param data the runtime values of function parameters
     * @return the function result
     */
    @Override
    protected Object execute(Object[] data) {
        return null;
    }

    /**
     * The main execution method which will be called upon event arrival
     * when there are zero or one function parameter
     *
     * @param data null if the function parameter count is zero or
     *             runtime data value of the function parameter
     * @return the function result
     */
    @Override
    protected Object execute(Object data) {
        if (data != null) {
            //type-conversion
            return converter.convert((Number) data);
        } else {
            throw new SiddhiAppRuntimeException("Input to the UnitConversion function cannot be null");
        }
    }

    @Override
    public Attribute.Type getReturnType() {
        return Attribute.Type.DOUBLE;
    }

    /**
     * Used to collect the serializable state of the processing element, that need to be
     * persisted for the reconstructing the element to the same state on a different point of time
     *
     * @return stateful objects of the processing element as an array
     */
    @Override
    public Map<String, Object> currentState() {
        return null;
    }

    /**
     * Used to restore serialized state of the processing element, for reconstructing
     * the element to the same state as if was on a previous point of time.
     *
     * @param state the stateful objects of the element as an array on
     *              the same order provided by currentState().
     */
    @Override
    public void restoreState(Map<String, Object> state) {
        //Implement restore state logic.
    }
}
