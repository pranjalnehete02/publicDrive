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
  },
})


export const { addFilenames } = usersFileSlice.actions

export default usersFileSlice.reducer