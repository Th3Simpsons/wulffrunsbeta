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

package de.nog;

/**
 * Simple real valued function interface.
 * 
 * @author nwulff
 * @since 22.10.2013
 * @version $Id: Function.java,v 1.4 2014/12/01 12:17:47 nwulff Exp $
 */
public interface Function {
    /**
     * Function evaluation mapping tuple (x1,...,xn) to y = f(x1,...,xn).
     * 
     * @param args the double array tuple x1,...,xn
     * @return y = f(x1,...,xn)
     */
    double eval(final double... args);
}
