package fr.epf.matmob.filmroll.model

data class ExtendedFilmInfo(
    val film: Film,
    val cast: List<Person>,
    val crew: List<Person>,
    val recommendations: List<LiteFilm>
)
