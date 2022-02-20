package br.com.meuorm.noorm;

import java.time.LocalDate;

public class VerificadorDeDatas {

	public Long diferencaDeDias(final LocalDate dataInicial, final LocalDate dataFinal) {
		Long longDataFinal = dataFinal.toEpochDay();
		Long longDataIncial = dataInicial.toEpochDay();
		return longDataFinal - longDataIncial;
	}

	public long diferencaDeMeses(final LocalDate dataInicial, final LocalDate dataFinal) {
		Long longDataFinal = dataFinal.toEpochDay();
		Long longDataInicial = dataInicial.toEpochDay();
		return (longDataFinal - longDataInicial) / 30;
	}

	public void testes(String msg, String... strings) {
		// var newMsg = msg;
		// for(byte i = 0; i < strings.length; i++) {
		// 	System.out.printf("i: %s - msg: %s \n", i, strings[i]);
		// 	newMsg = newMsg.replace("{}", strings[i]);
		// }
		// System.out.println("Nova Msg: " + newMsg);
	}
}
