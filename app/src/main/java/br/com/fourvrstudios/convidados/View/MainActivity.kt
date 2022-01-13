package br.com.fourvrstudios.convidados.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fourvrstudios.convidados.R

/*
Implementações realizadas por Tamara Salvatori

- Ação de vibrar o telefone ao mostrar alerta de que a lista toda poderá ser apagada
- Nomes da lista organizados por ordem alfabética
- Arquivo de strings -> implementação nos xmls ok; problemas ao substituir na ViewModel
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}