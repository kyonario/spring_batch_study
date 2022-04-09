package com.imd.exemplo_spring_batch_1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing //usado para importar todas as features do Spring Batch dentro da aplicação
@Configuration
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	//exemplo inicial
	//A lógica dentro do job é dividida em etapas, que chamamos de Step
	// Os Steps são encadeados para compor uma lógica maior.
	@Bean
	public Job imprimeOlaJob() {
		return jobBuilderFactory
				.get("imprimeOlaJob - demonstração que tá sendo utilizado o job") // nome dado ao job
				.start(imprimeOlaStep())
				.incrementer(new RunIdIncrementer()) //add um run id a cada execucao ou seja um parametro a cada execucao
				.build();
	}
	
	
	public Step imprimeOlaStep() {
		
		//para etapas simples usamos o tasklet: que são pequenas tarefas.
		return stepBuilderFactory
				.get("Imprimindo direto do método olá step") //nome dado ao Step
				.tasklet(imprimeOlaTasklet(null))
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet imprimeOlaTasklet(@Value("#{jobParameters['nome']}") String nome) { //pegando o argumento passado como paramentro na execucao 
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception{
				System.out.println(String.format("Olá, %s!", nome));
				return RepeatStatus.FINISHED;
			}
		};
	}

}
