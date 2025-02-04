import { createSlice } from '@reduxjs/toolkit'

export const usersFileSlice = createSlice({
  name: 'usersfile',
  initialState: {
    filenames: [],
  },
  reducers: {
    addFilenames: (state, action) => {
      state.filenames = action.payload
    },
    removeFilenames: (state)=>{
      state.filenames = [];
    }
  },
})


export const { addFilenames, removeFilenames } = usersFileSlice.actions

export default usersFileSlice.reducer