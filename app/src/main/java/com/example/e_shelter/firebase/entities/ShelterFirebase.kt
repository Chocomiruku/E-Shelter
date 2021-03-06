package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.Constants
import com.example.e_shelter.database.entities.Shelter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ShelterFirebase(private val reference: DatabaseReference) {
    val database = App.database.eShelterDatabaseDao

    init {
        subscribeOnDataChanges()
    }

    fun sendShelter(shelter: Shelter) {
        reference.child(Constants.SHELTERS_CHILD).child(shelter.id.toString()).setValue(shelter)
    }

    private fun subscribeOnDataChanges() {
        reference.child(Constants.SHELTERS_CHILD).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.getValue(Shelter::class.java) != null) {
                    if (database.getShelter(snapshot.getValue(Shelter::class.java)!!.id) == null) {
                        database.insert(snapshot.getValue(Shelter::class.java)!!)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                database.update(snapshot.getValue(Shelter::class.java)!!)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                database.deleteShelterById(snapshot.getValue(Shelter::class.java)!!.id)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}