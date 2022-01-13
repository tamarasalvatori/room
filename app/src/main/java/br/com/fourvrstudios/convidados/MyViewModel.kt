package br.com.fourvrstudios.convidados

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.fourvrstudios.convidados.db.Convidado
import br.com.fourvrstudios.convidados.db.Repositorio
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MyViewModel(private val repositorio: Repositorio) : ViewModel() {

    val convidados = repositorio.listaConvidados
    private var covidadoUpdateDelete: Convidado?

    val inputNome = MutableLiveData<String?>()
    val inputEmail = MutableLiveData<String?>()
    val inputBusca = MutableLiveData<String?>()

    val onClear = MutableLiveData<Boolean>()
    private var isUpdateOrDelete = false

    val saveUpdateBtnTxt = MutableLiveData<String>() // Texto do botão da esquerda save_or_update_button
    val clearDeleteBtnTxt = MutableLiveData<String>() // Texto do botão da direita clear clear_all_or_delete_button

    init {
        covidadoUpdateDelete = null
        saveUpdateBtnTxt.value =  "Salvar"
        clearDeleteBtnTxt.value = "Limpar"
        onClear.value = false
    }

    fun insert(convidado: Convidado): Job =
        viewModelScope.launch {
            repositorio.insert(convidado)
        }

    fun clearAll(): Job =
        viewModelScope.launch {
            repositorio.clearAll()
        }

    fun delete(convidado: Convidado): Job =
        viewModelScope.launch {
            repositorio.delete(convidado)
            inputNome.value = null
            inputEmail.value = null
            saveUpdateBtnTxt.value = "Salvar"
            clearDeleteBtnTxt.value = "Limpar"
            covidadoUpdateDelete = null
            isUpdateOrDelete = false
        }

    fun update(convidado: Convidado): Job =
        viewModelScope.launch {
            repositorio.update(convidado)
            inputNome.value = null
            inputEmail.value = null
            saveUpdateBtnTxt.value = "Salvar"
            clearDeleteBtnTxt.value = "Limpar"
            covidadoUpdateDelete = null
            isUpdateOrDelete = false

        }

    fun search(): Job =
        viewModelScope.launch {
            var convidadoEncontrado = repositorio.selectConvidado(inputBusca.value.toString())
            if (convidadoEncontrado != null) {
                    startUpdateDelete(convidadoEncontrado)
            }
        }

    fun startUpdateDelete(convidado: Convidado){
        inputNome.value = convidado.name
        inputEmail.value = convidado.email
        covidadoUpdateDelete = convidado

        saveUpdateBtnTxt.value = "Alterar"
        clearDeleteBtnTxt.value = "Deletar"
        isUpdateOrDelete = true
    }

    fun saveUpdate() {
        if (isUpdateOrDelete) {
            covidadoUpdateDelete?.name = inputNome.value!!
            covidadoUpdateDelete?.email = inputEmail.value!!
            update(covidadoUpdateDelete!!)
        } else {
            val nome = inputNome.value!!
            val email = inputEmail.value!!

            insert(Convidado(0,nome,email))
            inputNome.value = null
            inputEmail.value = null
        }
    }

    fun clearOrDelete() {
        if(isUpdateOrDelete) {
            delete(covidadoUpdateDelete!!)
        } else {
            setOnClearState(true)
            //clearAll()
        }
    }

    fun setOnClearState(state: Boolean){
        onClear.value = state
    }
}

// ViewModelFactory :  Uma classe que cria ViewModels (BOILERPLATE)
class ViewModelFactory(private val repositorio: Repositorio) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MyViewModel::class.java)){
            return MyViewModel(repositorio) as T // Retorna a ViewModel
        }
        throw IllegalArgumentException("ViewModel Desconhecida")
    }
}