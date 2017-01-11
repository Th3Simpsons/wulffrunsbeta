/*
 * Project: WRBRest
 *
 * Copyright (c) 2008-2016,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for computer sciences (Lab4Inf).
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

package de.lab4inf.wrb.rest;

import static java.util.Objects.requireNonNull;

/**
 * Generic immutable tuple of number pairs of Type T.
 *
 * @author nwulff
 * @since  08.12.2016
 * @version $Id: Pair.java,v 1.1 2017/01/06 17:18:01 nwulff Exp $
 * @param  <T> the generic number type.
 */
public final class Pair<T extends Number> {
    public final T x;
    public final T y;

    /**
     * Private hidden constructor.
     * @param x
     * @param y
     */
    private Pair(final T x, final T y) {
        this.x = x;
        this.y = y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%g,%g}", x.doubleValue(), y.doubleValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (null == o)
            return false;
        if (this == o)
            return true;
        if (getClass() == o.getClass()) {
            Pair<?> that = (Pair<?>) o;
            return this.x.equals(that.x) && this.y.equals(that.y);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return x.hashCode() ^ y.hashCode();
    }

    /**
     * Factory method to create a Pair<T>.
     * @param x 1.st parameter
     * @param y 2.nd parameter
     * @return Pair<T>
     * @param  <T> this generic type.
     */
    public static <T extends Number> Pair<T> pair(T x, T y) {
        return new Pair<>(requireNonNull(x), requireNonNull(y));
    }
}
