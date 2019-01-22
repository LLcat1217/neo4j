/*
 * Copyright (c) 2002-2019 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.runtime.interpreted.commands.expressions

import org.neo4j.cypher.internal.runtime.interpreted.{ExecutionContext, QueryStateHelper}
import org.neo4j.cypher.internal.v3_5.util.ArithmeticException
import org.neo4j.cypher.internal.v3_5.util.test_helpers.CypherFunSuite
import org.neo4j.values.storable.{NumberValue}

class DivideTest extends CypherFunSuite {
  test("should_throw_arithmetic_exception_for_divide_by_zero") {
    val ctx = ExecutionContext.empty
    val state = QueryStateHelper.empty

    intercept[ArithmeticException](Divide(Literal(1), Literal(0))(ctx, state))
    intercept[ArithmeticException](Divide(Literal(1.4), Literal(0))(ctx, state))
    // Floating point division should not throw "/ by zero".
    // The JVM does not trap IEEE-754 exceptional conditions (see https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.8.1)
    // The behaviour is defined as:
    Divide(Literal(1), Literal(0.0))(ctx, state).asInstanceOf[NumberValue].doubleValue() should equal(Double.PositiveInfinity)
    Divide(Literal(-1), Literal(0.0))(ctx, state).asInstanceOf[NumberValue].doubleValue() should equal(Double.NegativeInfinity)
    Divide(Literal(0), Literal(0.0))(ctx, state).asInstanceOf[NumberValue].isNaN shouldBe true
  }
}
