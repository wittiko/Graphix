package at.htlv.messner.graphix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import org.junit.BeforeClass;
import org.junit.Test;

import at.htlv.messner.graphix.model.Matrix;

public class MatrixTest 
{
	@Test
	public void whenWeUseTheDefaultConstructorWeShouldGet8AsDimension()
	{
		Matrix m = new Matrix();
		assertThat(m.getDimension(), is(7));
	}
	
	@Test
	public void whenWeSetTheDimensionWeShouldGetTheDimension()
	{
		Matrix m = new Matrix(5);
		assertThat(m.getDimension(), is(5));
	}

}
