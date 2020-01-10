package com.example.listeen.model

class Song (
    val id:String,
    val songName:String,
    val file_music: String,
    val id_album: String,
    val name_album: String,
    val id_singer: String,
    val name_singer: String){
    constructor():this("","","", "","", "","")
}