package de.neusta.ncc.application.validator.exception

class RoomNumberNotValidException constructor(val room: String?) : RuntimeException("Room with number $room must have 4 arbitrary characters.")