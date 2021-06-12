package br.com.dbserver.lista.retrofitcrudprodutos.model

import android.os.Parcel
import android.os.Parcelable

data class Produto (val id: String?, var nome: String, var preco: Double) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nome)
        parcel.writeDouble(preco)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Produto>{
            override fun createFromParcel(parcel: Parcel): Produto {
                return Produto(parcel)
            }

            override fun newArray(size: Int): Array<Produto?> {
                return arrayOfNulls(size)
            }
        }

    }
}