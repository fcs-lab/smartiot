import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IGeoLocation, defaultValue } from 'app/shared/model/geo-location.model';

const initialState: EntityState<IGeoLocation> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/geo-locations';

// Actions

export const getEntities = createAsyncThunk(
  'geoLocation/fetch_entity_list',
  async ({ page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<IGeoLocation[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'geoLocation/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IGeoLocation>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'geoLocation/create_entity',
  async (entity: IGeoLocation, thunkAPI) => {
    const result = await axios.post<IGeoLocation>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'geoLocation/update_entity',
  async (entity: IGeoLocation, thunkAPI) => {
    const result = await axios.put<IGeoLocation>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'geoLocation/partial_update_entity',
  async (entity: IGeoLocation, thunkAPI) => {
    const result = await axios.patch<IGeoLocation>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'geoLocation/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IGeoLocation>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const GeoLocationSlice = createEntitySlice({
  name: 'geoLocation',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = GeoLocationSlice.actions;

// Reducer
export default GeoLocationSlice.reducer;
