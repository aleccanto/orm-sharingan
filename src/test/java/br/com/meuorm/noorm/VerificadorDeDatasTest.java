package br.com.meuorm.noorm;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.sharingan.noorm.VerificadorDeDatas;

public class VerificadorDeDatasTest {

	VerificadorDeDatas verificadorDeDatas;

	@Before
	public void setUp() {
		this.verificadorDeDatas = new VerificadorDeDatas();
	}

	@Test
	public void deve_dizer_qual_a_diferencas_de_dias_entre_duas_datas_1() {

		long dias = verificadorDeDatas.diferencaDeDias(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1));

		Assert.assertEquals(31, dias);
	}

	@Test
	public void deve_dizer_qual_a_diferencas_de_dias_entre_duas_datas_2() {

		long dias = verificadorDeDatas.diferencaDeDias(LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1));

		Assert.assertEquals(29, dias);
	}

	@Test
	public void deve_dizer_qual_a_diferencas_de_dias_entre_duas_datas_3() {

		long dias = verificadorDeDatas.diferencaDeDias(LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1));

		Assert.assertEquals(3652, dias);
	}

	@Test
	public void deve_dizer_qual_a_diferenca_de_meses_entre_duas_datas() {

		long meses = verificadorDeDatas.diferencaDeMeses(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1));

		Assert.assertEquals(1, meses);

	}

	@Test
	public void deve_dizer_qual_a_diferenca_de_meses_entre_duas_datas_1() {

		long meses = verificadorDeDatas.diferencaDeMeses(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 1));

		Assert.assertEquals(2, meses);

	}

	@Test
	public void deve_dizer_qual_a_diferenca_de_meses_entre_duas_datas_2() {

		long meses = verificadorDeDatas.diferencaDeMeses(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 1));

		Assert.assertEquals(11, meses);
	}

	@Test
	public void test() {
		verificadorDeDatas.testes("Hello {}. Ol� {}", "world", "mundo");
	}

}
