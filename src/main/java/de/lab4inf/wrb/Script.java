/*
 * Project: WRB
 *
 * Copyright (c) 2008-2013,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for Computer Sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.lab4inf.wrb;

import java.io.IOException;
import java.io.InputStream;

/**
 * Common interface for the WRB parsing service.
 * @author  nwulff
 * @since   29.10.2013
 * @version $Id: Script.java,v 1.2 2014/10/11 13:10:06 nwulff Exp $
 */
public interface Script {
    /**
     * Get a named function.
     * @param name of the function
     * @return an implementation
     * @throws IllegalArgumentException in case of an unknown function
     */
    Function getFunction(final String name) throws IllegalArgumentException;
    /**
     * Get the value of a named variable.
     * @param name of the variable
     * @return the actual variable value
     * @throws IllegalArgumentException in case of an unknown variable
     */
    double getVariable(final String name) throws IllegalArgumentException;
    /**
     * Set the value of a named variable.
     * @param name of the variable
     * @param the new variable value
     */
    void setVariable(final String name, final double value);
    /**
     * Parse the given String into an AST and evaluate
     * the result via the tree walker. 
     * @param definition for the AST as String
     * @return the result of the parsed evaluation
     * @throws RecognitionException in case of a parse error
     * @throws IllegalArgumentException in case of an unknown variables or functions
     */
    double parse(final String definition) throws IllegalArgumentException;
    /**
     * Parse the given InputStream into an AST and evaluate
     * the result via the tree walker. 
     * @param defStream for the AST as InputStream
     * @return the result of the parsed evaluation
     * @throws RecognitionException in case of a parse error
     * @throws IOException in case of a stream error
     * @throws IllegalArgumentException in case of an unknown variables or functions
     */
    double parse(final InputStream defStream) throws IllegalArgumentException,IOException;
}
