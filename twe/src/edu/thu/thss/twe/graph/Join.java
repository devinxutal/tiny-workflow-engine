/*--

 Copyright (C) 2002-2005 Adrian Price.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The names "OBE" and "Open Business Engine" must not be used to
 	endorse or promote products derived from this software without prior
 	written permission.  For written permission, please contact
 	adrianprice@sourceforge.net.

 4. Products derived from this software may not be called "OBE" or
 	"Open Business Engine", nor may "OBE" or "Open Business Engine"
 	appear in their name, without prior written permission from
 	Adrian Price (adrianprice@users.sourceforge.net).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see
 <http://obe.sourceforge.net/>.

 */

package edu.thu.thss.twe.graph;


/**
 * A Join represents a location in a workflow process where two or more workflow
 * threads join together.  There are two types of joins: AND and XOR joins.  AND
 * joins require that all incoming threads complete before execution of the
 * workflow continues.  XOR joins only require one incoming thread to complete
 * before continuing execution.
 *
 * @author Adrian Price
 * 
 * @hibernate.mapping default-lazy="false"
 * @hibernate.meta attribute="class-description" value="Join"
 * @hibernate.class table="join"
 */

public final class Join{
	long id;
	
    private int joinType;

    /**
     * Constructs a new join.
     */
    public Join() {
    }
    
    
    /**
     * @hibernate.id generator-class="native" column="id"
     * @hibernate.meta attribute="field-description" value="ฑ๊สถ"
     */
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

    /**
     * @hibernate.property column="join_type" type="int" not-null="false"
     * @hibernate.meta attribute="field-description" value="JoinType"
     */
	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	public String toString() {
        return "Join[type=" + joinType + ']';
    }
}